-- Insert into carts (5 customers)
INSERT INTO carts (customer_id)
VALUES (1), (2), (3), (4), (5);

-- Insert cart items for each cart
-- Cart 1 (Customer 1)
INSERT INTO cart_items (cart_id, moment_id, quantity)
VALUES (1, 3, 2),  -- Comedy Show
       (1, 5, 1),  -- Indie Rock Live
       (1, 20, 3); -- Open Mic Night

-- Cart 2 (Customer 2)
INSERT INTO cart_items (cart_id, moment_id, quantity)
VALUES (2, 4, 2),   -- Food Festival
       (2, 10, 2),  -- Indie Rock Live
       (2, 18, 1);  -- Piano and Poetry

-- Cart 3 (Customer 3)
INSERT INTO cart_items (cart_id, moment_id, quantity)
VALUES (3, 2, 1),   -- Art Exhibition
       (3, 9, 1),   -- Farmers Market Feast
       (3, 24, 2);  -- Satirical Sketch Show

-- Cart 4 (Customer 4)
INSERT INTO cart_items (cart_id, moment_id, quantity)
VALUES (4, 12, 2),  -- Comedy & Craft Beer
       (4, 25, 4);  -- Dessert Carnival

-- Cart 5 (Customer 5)
INSERT INTO cart_items (cart_id, moment_id, quantity)
VALUES (5, 14, 1),  -- Electronic Beats
       (5, 6, 2),   -- Sunset Acoustic Session
       (5, 1, 2),   -- Concert in the Park
       (5, 17, 1),  -- Vegan Taste Tour
       (5, 24, 65); -- Painting in the park (should cause Insufficient stock error)

-- delete from cart_items where cart_id = 5;
