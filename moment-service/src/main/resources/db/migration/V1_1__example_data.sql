-- Insert into categories
INSERT INTO categories (name, description, created_by, updated_by)
VALUES ('Music', 'Music-related events', 'admin', 'admin'),
       ('Art', 'Art-related events', 'admin', 'admin'),
       ('Comedy', 'Comedy shows and events', 'admin', 'admin'),
       ('Food', 'Food festivals and events', 'admin', 'admin');

-- Insert into locations
INSERT INTO locations (city, address, created_by, updated_by)
VALUES ('New York', '123 Main St', 'admin', 'admin'),
       ('Los Angeles', '456 Sunset Blvd', 'admin', 'admin'),
       ('Chicago', '789 Lakeshore Dr', 'admin', 'admin'),
       ('San Francisco', '101 Market St', 'admin', 'admin');

-- Insert into moments
INSERT INTO moments (host_id, category_id, location_id, title, start_date, recurrence, price,
                     status, ticket_count, created_by, updated_by)
VALUES (1, 1, 1, 'Concert in the Park', '2025-06-01 19:00:00', 'ONETIME', 50.00, 'LIVE', 500,
        'admin', 'admin'),
       (2, 2, 2, 'Art Exhibition', '2025-07-10 18:00:00', 'REGULAR', 25.00, 'LIVE', 200,
        'admin', 'admin'),
       (3, 3, 3, 'Comedy Show', '2025-08-15 20:00:00', 'ONETIME', 30.00, 'DRAFT', 150, 'admin',
        'admin'),
       (4, 4, 4, 'Food Festival', '2025-09-05 10:00:00', 'REGULAR', 15.00, 'LIVE', 800, 'admin',
        'admin');

-- Insert into moment_details
INSERT INTO moment_details (moment_id, description, created_by, updated_by)
VALUES (1, 'A live music concert in the park featuring local bands.', 'admin', 'admin'),
       (2, 'An art exhibition showcasing modern art pieces.', 'admin', 'admin'),
       (3, 'A stand-up comedy show with popular comedians.', 'admin', 'admin'),
       (4, 'A food festival with various cuisines and food trucks.', 'admin', 'admin');