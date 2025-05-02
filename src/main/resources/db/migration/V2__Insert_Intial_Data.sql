-- Insert sample venues
INSERT INTO venues (name, address, city, state, country, postal_code, capacity, description, contact_phone, contact_email, website_url, image_url)
VALUES 
('Impact Arena', 'Popular Road, Pak Kret', 'Bangkok', NULL, 'Thailand', '11120', 12000, 'One of Thailand''s largest indoor arenas', '02-833-4455', 'info@impact.co.th', 'https://www.impact.co.th', 'https://example.com/venues/impact.jpg'),
('Thunder Dome', 'Muang Thong Thani', 'Bangkok', NULL, 'Thailand', '11120', 5000, 'Popular concert venue in Bangkok', '02-123-4567', 'info@thunderdome.co.th', 'https://www.thunderdome.co.th', 'https://example.com/venues/thunderdome.jpg'),
('Central World', 'Ratchadamri Road, Pathum Wan', 'Bangkok', NULL, 'Thailand', '10330', 3000, 'Shopping mall with event space', '02-640-7000', 'info@centralworld.co.th', 'https://www.centralworld.co.th', 'https://example.com/venues/centralworld.jpg');

-- Insert sample artists
INSERT INTO artists (name, bio, genre, country, website_url, social_media_url, image_url)
VALUES 
('BNK48', 'Thai idol girl group based in Bangkok', 'Pop', 'Thailand', 'https://www.bnk48.com', 'https://www.instagram.com/bnk48official', 'https://example.com/artists/bnk48.jpg'),
('Slot Machine', 'Thai rock band formed in 2000', 'Rock', 'Thailand', 'https://www.slotmachine.band', 'https://www.facebook.com/slotmachineband', 'https://example.com/artists/slotmachine.jpg'),
('Bodyslam', 'Popular Thai rock band', 'Rock', 'Thailand', 'https://www.bodyslam.th', 'https://www.instagram.com/bodyslamband', 'https://example.com/artists/bodyslam.jpg'),
('The Toys', 'Thai singer-songwriter', 'Pop', 'Thailand', 'https://www.thetoys.th', 'https://www.facebook.com/thetoysofficial', 'https://example.com/artists/thetoys.jpg');

-- Insert sample organizers
INSERT INTO organizers (name, description, contact_email, contact_phone, website_url, logo_url, address, tax_id)
VALUES 
('BNK48 Office', 'Official management company for BNK48', 'contact@bnk48office.com', '02-123-4567', 'https://www.bnk48office.com', 'https://example.com/organizers/bnk48office.jpg', 'Bangkok, Thailand', 'TH1234567890'),
('Genie Records', 'Thai record label and event organizer', 'contact@genierecords.com', '02-234-5678', 'https://www.genierecords.com', 'https://example.com/organizers/genierecords.jpg', 'Bangkok, Thailand', 'TH0987654321'),
('Bec-Tero Entertainment', 'Leading entertainment company in Thailand', 'contact@bectero.com', '02-345-6789', 'https://www.bectero.com', 'https://example.com/organizers/bectero.jpg', 'Bangkok, Thailand', 'TH5678901234');

-- Insert sample events
INSERT INTO events (name, description, venue_id, date, time, duration, organizer_id, category, status, image_url)
VALUES 
('BNK48 Concert 2025', 'BNK48 Annual Concert at Impact Arena', 1, '2025-06-15', '18:00', 180, 1, 'Concert', 'UPCOMING', 'https://example.com/events/bnk48.jpg'),
('BNK48 Handshake Event', 'Meet & Greet with BNK48 members', 3, '2025-06-20', '14:00', 240, 1, 'Fan Meeting', 'UPCOMING', 'https://example.com/events/bnk48_handshake.jpg'),
('Slot Machine Live in Bangkok', 'Rock concert featuring Slot Machine', 2, '2025-07-10', '19:30', 150, 2, 'Concert', 'UPCOMING', 'https://example.com/events/slotmachine.jpg'),
('Thai Rock Festival 2025', 'Featuring Bodyslam, The Toys, and more', 1, '2025-08-20', '16:00', 360, 3, 'Festival', 'UPCOMING', 'https://example.com/events/rockfestival.jpg'),
('Music in the Park', 'Outdoor music event with various artists', 3, '2025-05-10', '17:00', 240, 2, 'Festival', 'DRAFT', 'https://example.com/events/musicpark.jpg');

-- Link events to artists
INSERT INTO event_artists (event_id, artist_id)
VALUES 
(1, 1), -- BNK48 Concert with BNK48
(2, 1), -- BNK48 Handshake with BNK48
(3, 2), -- Slot Machine Live with Slot Machine
(4, 3), -- Thai Rock Festival with Bodyslam
(4, 4), -- Thai Rock Festival with The Toys
(5, 2), -- Music in the Park with Slot Machine
(5, 4); -- Music in the Park with The Toys

-- Insert ticket types for each event
INSERT INTO ticket_types (event_id, type, price, quantity, max_per_purchase, sale_start_time, sale_end_time)
VALUES 
-- BNK48 Concert
(1, 'VIP', 5000.00, 500, 4, '2025-04-15 10:00:00', '2025-06-14 23:59:59'),
(1, 'Regular', 2500.00, 5000, 8, '2025-04-15 10:00:00', '2025-06-14 23:59:59'),
(1, 'Economy', 1500.00, 6500, 10, '2025-04-15 10:00:00', '2025-06-14 23:59:59'),

-- BNK48 Handshake
(2, 'Regular', 1500.00, 2000, 5, '2025-05-20 10:00:00', '2025-06-19 23:59:59'),

-- Slot Machine Live
(3, 'VIP', 3500.00, 1000, 4, '2025-05-10 10:00:00', '2025-07-09 23:59:59'),
(3, 'Regular', 2000.00, 4000, 8, '2025-05-10 10:00:00', '2025-07-09 23:59:59'),

-- Thai Rock Festival
(4, 'VIP', 4500.00, 2000, 4, '2025-06-01 10:00:00', '2025-08-19 23:59:59'),
(4, 'Regular', 2500.00, 6000, 8, '2025-06-01 10:00:00', '2025-08-19 23:59:59'),
(4, 'Standing', 1800.00, 4000, 10, '2025-06-01 10:00:00', '2025-08-19 23:59:59'),

-- Music in the Park
(5, 'Regular', 1200.00, 3000, 10, '2025-04-01 10:00:00', '2025-05-09 23:59:59');

-- Insert ticket benefits
INSERT INTO ticket_benefits (ticket_type_id, benefit)
VALUES 
(1, 'Meet & Greet'),
(1, 'Exclusive Merchandise'),
(1, 'Priority Entry'),
(1, 'Signed Poster'),
(2, 'Concert Program'),
(2, 'Priority Entry'),
(5, 'Meet & Greet'),
(5, 'Exclusive Merchandise'),
(7, 'VIP Lounge Access'),
(7, 'Free Drinks'),
(7, 'Exclusive Festival Merchandise');

-- Insert sample status updates for events
INSERT INTO event_status_updates (event_id, field, old_value, new_value, reason, notification_status)
VALUES 
(1, 'venue', 'Thunder Dome', 'Impact Arena', 'Venue change due to higher than expected ticket demand', 'COMPLETED'),
(3, 'date', '2025-07-05', '2025-07-10', 'Scheduling conflict with another event', 'COMPLETED'),
(4, 'time', '17:00', '16:00', 'Adjusted to accommodate more performances', 'COMPLETED');

-- Insert notification records for status updates
INSERT INTO status_update_notifications (update_id, notification_type, sent)
VALUES 
(1, 'email', true),
(1, 'sms', true),
(1, 'push', true),
(2, 'email', true),
(2, 'sms', true),
(2, 'push', true),
(3, 'email', true),
(3, 'sms', true),
(3, 'push', true);