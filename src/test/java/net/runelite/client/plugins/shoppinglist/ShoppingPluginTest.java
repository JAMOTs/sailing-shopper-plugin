package net.runelite.client.plugins.shoppinglist;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import net.runelite.client.plugins.shoppinglist.ShoppingPlugin.ShoppingItem;

public class ShoppingPluginTest
{
	@Test
	public void testParseShoppingItem()
	{
		// Case 1: "itemName x quantity"
		ShoppingItem item1 = ShoppingPlugin.parseShoppingItem("Oak plank x 8");
		assertEquals("Oak plank", item1.name);
		assertEquals(8, item1.quantity);

		// Case 2: "quantity x itemName"
		ShoppingItem item2 = ShoppingPlugin.parseShoppingItem("8 x Oak plank");
		assertEquals("Oak plank", item2.name);
		assertEquals(8, item2.quantity);

		// Case 3: Single item
		ShoppingItem item3 = ShoppingPlugin.parseShoppingItem("Hammer");
		assertEquals("Hammer", item3.name);
		assertEquals(1, item3.quantity);

		// Case 4: Item with spaces in name
		ShoppingItem item4 = ShoppingPlugin.parseShoppingItem("Bolt of cloth x 10");
		assertEquals("Bolt of cloth", item4.name);
		assertEquals(10, item4.quantity);

		// Case 5: Trimming
		ShoppingItem item5 = ShoppingPlugin.parseShoppingItem("  Hammer  ");
		// The parse method in Plugin trims BEFORE calling parseShoppingItem? 
		// Wait, looking at the code:
		// String cleanText = Text.removeTags(rawText).trim();
		// ShoppingItem item = parseShoppingItem(cleanText);
		// So the input to parseShoppingItem is already trimmed of outer whitespace.
		// But if I pass untrimmed, my parse logic splits by " x ". 
		// "  Hammer  " -> "  Hammer  " (if no " x ")
		// Let's test if the parser handles internal whitespace issues or if we rely on caller.
		// The parser logic: "quantity = Integer.parseInt(parts[0].trim());"
		// It trims the PARTS.
		
		// If I pass "  Oak plank x 8  ", split(" x ") might give ["  Oak plank", "8  "]
		// Then trim() fixes it.
		ShoppingItem item5b = ShoppingPlugin.parseShoppingItem("  Oak plank x 8  ");
		assertEquals("Oak plank", item5b.name);
		assertEquals(8, item5b.quantity);

		// Case 6: Fallback
		ShoppingItem item6 = ShoppingPlugin.parseShoppingItem("Invalid x Format");
		// "Invalid" is not int, "Format" is not int.
		// Catch blocks should catch it and return original text, quantity 1.
		assertEquals("Invalid x Format", item6.name);
		assertEquals(1, item6.quantity);
	}
}
