-- Seed data loaded automatically on startup (H2 dev mode)
-- Tables are created by Hibernate before this runs

-- ── Demo Organization ──
INSERT INTO organizations (name, ein, mission_statement, annual_budget, exec_director_name, exec_director_email, phone, city, state, tax_status)
VALUES ('GreenPath Foundation', '47-1234567', 'Empowering youth through environmental stewardship and education.', 1200000.00, 'Sara Hoffman', 'sara@greenpath.org', '404-555-0100', 'Atlanta', 'GA', 'NONPROFIT_501C3');

-- ── Demo User (password = "password123") ──
INSERT INTO app_users (email, password_hash, full_name, role, org_id)
VALUES ('sara@greenpath.org', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE.8cFBP9ulCwWZKS', 'Sara Hoffman', 'NONPROFIT', 1);

-- ── Funders ──
INSERT INTO funders (name, description, min_grant, max_grant, focus_areas, geographic_focus, next_deadline, active)
VALUES
  ('Patagonia Environmental Fund', 'Supporting grassroots environmental groups working on climate, biodiversity, and sustainability.', 10000, 60000, 'Environment,Youth,Community', 'National', '2025-04-15', true),
  ('Gates Foundation', 'Dedicated to global education, health equity, and poverty alleviation through innovation.', 25000, 100000, 'Education,STEM,Health,Global', 'Global', '2025-05-01', true),
  ('Robert Wood Johnson Foundation', 'Building a culture of health and equity in the United States.', 20000, 150000, 'Health,Housing,Equity', 'National', '2025-03-30', true),
  ('Kellogg Foundation', 'Committed to thriving children, working families, and equitable communities.', 15000, 95000, 'Health,Food,Community,Rural', 'National', '2025-04-30', true),
  ('Bloomberg Philanthropies', 'Investing in climate, public health, arts, government innovation, and education.', 30000, 120000, 'Climate,Education,Arts,Equity', 'National', '2025-06-15', true),
  ('Ford Foundation', 'Focused on reducing inequality across all its dimensions.', 20000, 80000, 'Social Justice,Arts,Education', 'Global', '2025-05-20', true);
