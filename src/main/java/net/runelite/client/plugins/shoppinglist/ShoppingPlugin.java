package net.runelite.client.plugins.shoppinglist;

import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

@PluginDescriptor(
	name = "Sailing Shopper"
)
public class ShoppingPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	private ShoppingPanel panel;
	private NavigationButton navButton;

	// TODO: UPDATE THIS ID USING THE INSPECTOR!
	// 214 is the standard 'Skill Guide' ID, but Sailing might use a new one.
	// Use the Dev Tools -> Widget Inspector to verify.
	private static final int CONSTRUCTION_GUIDE_ID = 214;
	private static final int SAILING_GUIDE_ID = 860; // InterfaceID.SKILL_GUIDE_V2

	@Override
	protected void startUp()
	{
		panel = new ShoppingPanel();
		navButton = NavigationButton.builder()
			.tooltip("Shopping List")
			.icon(ImageUtil.loadImageResource(getClass(), "icon.png"))
			.panel(panel)
			.build();
		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{
		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
		int widgetId = event.getFirstEntry().getParam1();
		int groupId = WidgetUtil.componentToInterface(widgetId);

		// Check if we are in the Construction OR Sailing guide
		if (groupId == CONSTRUCTION_GUIDE_ID || groupId == SAILING_GUIDE_ID)
		{
			client.createMenuEntry(-1)
				.setOption("Add materials to Shopping List")
				.setTarget(event.getFirstEntry().getTarget())
				.setType(MenuAction.RUNELITE)
				.onClick(this::onAddToShoppingList);
		}
	}

	private void onAddToShoppingList(MenuEntry entry)
	{
		// The widget ID of the clicked element
		int clickedWidgetId = entry.getParam1();
		Widget w = client.getWidget(clickedWidgetId);

		if (w != null)
		{
			// Attempt to parse the item count and name
			String rawText = w.getText();
			String cleanText = Text.removeTags(rawText).trim(); // Trim to remove leading/trailing spaces

			ShoppingItem item = parseShoppingItem(cleanText);

			panel.addItem(item.name, item.quantity);

			// Visual feedback
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "",
				"Added " + item.name + " (" + item.quantity + ") to shopping list.", "");
		}
	}

	public static ShoppingItem parseShoppingItem(String cleanText)
	{
		String itemName = cleanText;
		int quantity = 1;

		// Try to parse "itemName x quantity" or "quantity x itemName"
		String[] parts = cleanText.split(" x ");
		if (parts.length == 2)
		{
			try
			{
				// Case: "quantity x itemName"
				quantity = Integer.parseInt(parts[0].trim());
				itemName = parts[1].trim();
			}
			catch (NumberFormatException e)
			{
				// Case: "itemName x quantity"
				try
				{
					quantity = Integer.parseInt(parts[1].trim());
					itemName = parts[0].trim();
				}
				catch (NumberFormatException ex)
				{
					// Neither part is a number, treat as a single item with quantity 1
					quantity = 1;
					itemName = cleanText;
				}
			}
		}
		
		return new ShoppingItem(itemName, quantity);
	}

	public static class ShoppingItem
	{
		public final String name;
		public final int quantity;

		public ShoppingItem(String name, int quantity)
		{
			this.name = name;
			this.quantity = quantity;
		}
	}
}
