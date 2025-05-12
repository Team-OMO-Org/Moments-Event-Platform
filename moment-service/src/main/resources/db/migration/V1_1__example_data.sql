-- Insert into categories
    INSERT INTO categories (name, description)
    VALUES ('Music', 'Music-related events'),
           ('Art', 'Art-related events'),
           ('Comedy', 'Comedy shows and events'),
           ('Food', 'Food festivals and events');

    -- Insert into locations
    INSERT INTO locations (city, address)
    VALUES ('New York', '123 Main St'),
           ('Los Angeles', '456 Sunset Blvd'),
           ('Chicago', '789 Lakeshore Dr'),
           ('San Francisco', '101 Market St');

    -- Insert into moments
    INSERT INTO moments (host_id, category_id, location_id, title, short_description, start_date, recurrence, price, status, ticket_count)
    VALUES (1, 1, 1, 'Concert in the Park', 'A live music concert in the park.', '2025-06-01 19:00:00', 'ONETIME', 50.00, 'LIVE', 500),
           (2, 2, 2, 'Art Exhibition', 'An art exhibition showcasing modern art.', '2025-07-10 18:00:00', 'REGULAR', 25.00, 'LIVE', 200),
           (3, 3, 3, 'Comedy Show', 'A stand-up comedy show with popular comedians.', '2025-08-15 20:00:00', 'ONETIME', 30.00, 'DRAFT', 150),
           (4, 4, 4, 'Food Festival', 'A food festival with various cuisines.', '2025-09-05 10:00:00', 'REGULAR', 15.00, 'LIVE', 800);

    -- Insert into moment_details
    INSERT INTO moment_details (moment_id, description)
    VALUES (1, 'A live music concert in the park featuring local bands.'),
           (2, 'An art exhibition showcasing modern art pieces.'),
           (3, 'A stand-up comedy show with popular comedians.'),
           (4, 'A food festival with various cuisines and food trucks.');