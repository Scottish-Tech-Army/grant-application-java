-- =============================================================================
-- data.sql  –  Sample data for the Grant Application app (H2 in-memory)
-- Loaded automatically by Spring Boot on every startup.
-- UUIDs are fixed so foreign-key references stay consistent across runs.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- USERS
-- -----------------------------------------------------------------------------
INSERT INTO users (id, email, password_hash, first_name, last_name, active, created_date) VALUES
(
    'aaaaaaaa-0001-0001-0001-000000000001',
    'alice@example.com',
    'password123',
    'Alice',
    'Johnson',
    TRUE,
    '2026-01-15'
),
(
    'bbbbbbbb-0002-0002-0002-000000000002',
    'bob@example.com',
    'password456',
    'Bob',
    'Smith',
    TRUE,
    '2026-01-15'
);

-- -----------------------------------------------------------------------------
-- USER_PREDEFINED_FIELDS
-- Each row stores the entire list of versioned field items as a JSON CLOB.
-- -----------------------------------------------------------------------------
INSERT INTO user_predefined_fields (id, user_id, created_date, fields_json) VALUES
(
    'cccccccc-0001-0001-0001-000000000001',
    'aaaaaaaa-0001-0001-0001-000000000001',
    '2026-01-15',
    '[
      {"fieldKey":"Organisation Name","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Alice Johnson Charity","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Registered Charity Number","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"1234567","timeStamp":"2026-01-15"},"2":{"value":"7654321","timeStamp":"2026-03-01"}}},
      {"fieldKey":"Organisation Address","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"123 High Street, London, EC1A 1BB","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Organisation Email","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"alice@example.com","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Organisation Phone","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"+44 20 7946 0958","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Organisation Website","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"https://www.alicecharity.org","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Year Founded","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"2010","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Total Full-Time Staff","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"12","timeStamp":"2026-01-15"},"2":{"value":"15","timeStamp":"2026-03-10"}}},
      {"fieldKey":"Total Part-Time Staff","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"5","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Annual Turnover (£)","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"250000","timeStamp":"2026-01-15"},"2":{"value":"310000","timeStamp":"2026-02-20"}}}
    ]'
),
(
    'cccccccc-0002-0002-0002-000000000002',
    'bbbbbbbb-0002-0002-0002-000000000002',
    '2026-01-15',
    '[
      {"fieldKey":"Organisation Name","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Bob Smith Charity","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Registered Charity Number","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"1234567","timeStamp":"2026-01-15"},"2":{"value":"7654321","timeStamp":"2026-03-01"}}},
      {"fieldKey":"Organisation Address","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"123 High Street, London, EC1A 1BB","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Organisation Email","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"bob@example.com","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Organisation Phone","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"+44 20 7946 0958","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Organisation Website","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"https://www.bobcharity.org","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Year Founded","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"2010","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Total Full-Time Staff","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"12","timeStamp":"2026-01-15"},"2":{"value":"15","timeStamp":"2026-03-10"}}},
      {"fieldKey":"Total Part-Time Staff","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"5","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Annual Turnover (£)","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"250000","timeStamp":"2026-01-15"},"2":{"value":"310000","timeStamp":"2026-02-20"}}}
    ]'
);

-- -----------------------------------------------------------------------------
-- USER_PREDEFINED_QUESTIONS
-- -----------------------------------------------------------------------------
INSERT INTO user_predefined_questions (id, user_id, created_date, questions_json) VALUES
(
    'dddddddd-0001-0001-0001-000000000001',
    'aaaaaaaa-0001-0001-0001-000000000001',
    '2026-01-15',
    '[
      {"fieldKey":"Mission Statement","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"We empower underserved communities through education and digital skills training.","timeStamp":"2026-01-15"},"2":{"value":"We empower underserved communities through education, digital skills, and employment pathways.","timeStamp":"2026-02-10"}}},
      {"fieldKey":"What the Organisation Does","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We deliver free coding bootcamps, mentoring programmes, and job-readiness workshops to young people aged 16-25 in deprived areas.","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Target Beneficiaries","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Young people aged 16-25 from low-income households, particularly those not in education, employment, or training (NEET).","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Geographic Area of Operation","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"Greater London and the South East of England.","timeStamp":"2026-01-15"},"2":{"value":"Greater London, South East England, and the West Midlands.","timeStamp":"2026-03-05"}}},
      {"fieldKey":"How has COVID-19 Impacted Your Work","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"COVID-19 accelerated our pivot to online delivery. We moved all bootcamps to virtual platforms, which increased reach by 40% but required significant investment in digital infrastructure.","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Measurement and Evaluation Approach","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We track participant outcomes at 3, 6, and 12 months post-programme, measuring employment rates, income changes, and self-reported wellbeing scores.","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Collaborative Partnerships","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We work closely with local councils, DWP job centres, Tech Nation, and a network of 30+ employer partners who provide mentors and job placements.","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Safeguarding Policy","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Our safeguarding policy is reviewed annually by the board. All staff and volunteers hold current DBS checks. A designated safeguarding lead is in post.","timeStamp":"2026-01-15"}}}
    ]'
),
(
    'dddddddd-0002-0002-0002-000000000002',
    'bbbbbbbb-0002-0002-0002-000000000002',
    '2026-01-15',
    '[
      {"fieldKey":"Mission Statement","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"We empower underserved communities through education and digital skills training.","timeStamp":"2026-01-15"},"2":{"value":"We empower underserved communities through education, digital skills, and employment pathways.","timeStamp":"2026-02-10"}}},
      {"fieldKey":"What the Organisation Does","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We deliver free coding bootcamps, mentoring programmes, and job-readiness workshops to young people aged 16-25 in deprived areas.","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Target Beneficiaries","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Young people aged 16-25 from low-income households, particularly those not in education, employment, or training (NEET).","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Geographic Area of Operation","type":"PREDEFINED","currentVersion":2,"versions":{"1":{"value":"Greater London and the South East of England.","timeStamp":"2026-01-15"},"2":{"value":"Greater London, South East England, and the West Midlands.","timeStamp":"2026-03-05"}}},
      {"fieldKey":"How has COVID-19 Impacted Your Work","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"COVID-19 accelerated our pivot to online delivery. We moved all bootcamps to virtual platforms, which increased reach by 40% but required significant investment in digital infrastructure.","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Measurement and Evaluation Approach","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We track participant outcomes at 3, 6, and 12 months post-programme, measuring employment rates, income changes, and self-reported wellbeing scores.","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Collaborative Partnerships","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We work closely with local councils, DWP job centres, Tech Nation, and a network of 30+ employer partners who provide mentors and job placements.","timeStamp":"2026-01-15"}}},
      {"fieldKey":"Safeguarding Policy","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Our safeguarding policy is reviewed annually by the board. All staff and volunteers hold current DBS checks. A designated safeguarding lead is in post.","timeStamp":"2026-01-15"}}}
    ]'
);

-- -----------------------------------------------------------------------------
-- APPLICATIONS  (3 per user: SUBMITTED, DRAFT, APPROVED)
-- -----------------------------------------------------------------------------

-- Alice – App 1: SUBMITTED
INSERT INTO applications (id, user_id, name, funder_name, status, start_date, end_date, create_date, fields_json, questions_json) VALUES
(
    'eeeeeeee-a001-a001-a001-000000000001',
    'aaaaaaaa-0001-0001-0001-000000000001',
    'Digital Skills Bootcamp 2026',
    'National Lottery Community Fund',
    'SUBMITTED',
    '2026-04-01',
    '2027-03-31',
    '2026-02-01',
    '[
      {"fieldKey":"Organisation Name","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Alice Johnson Charity","timeStamp":"2026-02-01"}}},
      {"fieldKey":"Requested Amount (£)","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"75000","timeStamp":"2026-02-01"}}},
      {"fieldKey":"Project Duration (months)","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"12","timeStamp":"2026-02-01"}}}
    ]',
    '[
      {"fieldKey":"Mission Statement","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We empower underserved communities through education, digital skills, and employment pathways.","timeStamp":"2026-02-01"}}},
      {"fieldKey":"Project Description","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"A 12-month intensive digital skills programme delivering 6 cohorts of 20 participants each, focusing on web development, data literacy, and AI tools.","timeStamp":"2026-02-01"}}},
      {"fieldKey":"Expected Outcomes","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"120 young people trained; 70% securing employment or further education within 6 months of completion.","timeStamp":"2026-02-01"}}}
    ]'
);

-- Alice – App 2: DRAFT
INSERT INTO applications (id, user_id, name, funder_name, status, start_date, end_date, create_date, fields_json, questions_json) VALUES
(
    'eeeeeeee-a002-a002-a002-000000000002',
    'aaaaaaaa-0001-0001-0001-000000000001',
    'Community Mentoring Network',
    'City Bridge Foundation',
    'DRAFT',
    '2026-07-01',
    '2027-06-30',
    '2026-03-15',
    '[
      {"fieldKey":"Organisation Name","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Alice Johnson Charity","timeStamp":"2026-03-15"}}},
      {"fieldKey":"Requested Amount (£)","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"45000","timeStamp":"2026-03-15"}}}
    ]',
    '[
      {"fieldKey":"Mission Statement","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We empower underserved communities through education, digital skills, and employment pathways.","timeStamp":"2026-03-15"}}},
      {"fieldKey":"Project Description","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"A peer mentoring network connecting 100 young people with professional mentors from the tech industry over 12 months.","timeStamp":"2026-03-15"}}}
    ]'
);

-- Alice – App 3: APPROVED
INSERT INTO applications (id, user_id, name, funder_name, status, start_date, end_date, create_date, fields_json, questions_json) VALUES
(
    'eeeeeeee-a003-a003-a003-000000000003',
    'aaaaaaaa-0001-0001-0001-000000000001',
    'Youth Employment Pathways',
    'Esmée Fairbairn Foundation',
    'APPROVED',
    '2025-10-01',
    '2026-09-30',
    '2025-08-10',
    '[
      {"fieldKey":"Organisation Name","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Alice Johnson Charity","timeStamp":"2025-08-10"}}},
      {"fieldKey":"Requested Amount (£)","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"60000","timeStamp":"2025-08-10"}}},
      {"fieldKey":"Number of Beneficiaries","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"80","timeStamp":"2025-08-10"}}}
    ]',
    '[
      {"fieldKey":"Mission Statement","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We empower underserved communities through education and digital skills training.","timeStamp":"2025-08-10"}}},
      {"fieldKey":"Collaborative Partnerships","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We work closely with local councils, DWP job centres, Tech Nation, and a network of 30+ employer partners who provide mentors and job placements.","timeStamp":"2025-08-10"}}},
      {"fieldKey":"Measurement and Evaluation Approach","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We track participant outcomes at 3, 6, and 12 months post-programme, measuring employment rates, income changes, and self-reported wellbeing scores.","timeStamp":"2025-08-10"}}}
    ]'
);

-- Bob – App 1: SUBMITTED
INSERT INTO applications (id, user_id, name, funder_name, status, start_date, end_date, create_date, fields_json, questions_json) VALUES
(
    'ffffffff-b001-b001-b001-000000000001',
    'bbbbbbbb-0002-0002-0002-000000000002',
    'Digital Skills Bootcamp 2026',
    'National Lottery Community Fund',
    'SUBMITTED',
    '2026-04-01',
    '2027-03-31',
    '2026-02-01',
    '[
      {"fieldKey":"Organisation Name","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Bob Smith Charity","timeStamp":"2026-02-01"}}},
      {"fieldKey":"Requested Amount (£)","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"75000","timeStamp":"2026-02-01"}}},
      {"fieldKey":"Project Duration (months)","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"12","timeStamp":"2026-02-01"}}}
    ]',
    '[
      {"fieldKey":"Mission Statement","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We empower underserved communities through education, digital skills, and employment pathways.","timeStamp":"2026-02-01"}}},
      {"fieldKey":"Project Description","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"A 12-month intensive digital skills programme delivering 6 cohorts of 20 participants each, focusing on web development, data literacy, and AI tools.","timeStamp":"2026-02-01"}}},
      {"fieldKey":"Expected Outcomes","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"120 young people trained; 70% securing employment or further education within 6 months of completion.","timeStamp":"2026-02-01"}}}
    ]'
);

-- Bob – App 2: DRAFT
INSERT INTO applications (id, user_id, name, funder_name, status, start_date, end_date, create_date, fields_json, questions_json) VALUES
(
    'ffffffff-b002-b002-b002-000000000002',
    'bbbbbbbb-0002-0002-0002-000000000002',
    'Community Mentoring Network',
    'City Bridge Foundation',
    'DRAFT',
    '2026-07-01',
    '2027-06-30',
    '2026-03-15',
    '[
      {"fieldKey":"Organisation Name","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Bob Smith Charity","timeStamp":"2026-03-15"}}},
      {"fieldKey":"Requested Amount (£)","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"45000","timeStamp":"2026-03-15"}}}
    ]',
    '[
      {"fieldKey":"Mission Statement","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We empower underserved communities through education, digital skills, and employment pathways.","timeStamp":"2026-03-15"}}},
      {"fieldKey":"Project Description","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"A peer mentoring network connecting 100 young people with professional mentors from the tech industry over 12 months.","timeStamp":"2026-03-15"}}}
    ]'
);

-- Bob – App 3: APPROVED
INSERT INTO applications (id, user_id, name, funder_name, status, start_date, end_date, create_date, fields_json, questions_json) VALUES
(
    'ffffffff-b003-b003-b003-000000000003',
    'bbbbbbbb-0002-0002-0002-000000000002',
    'Youth Employment Pathways',
    'Esmée Fairbairn Foundation',
    'APPROVED',
    '2025-10-01',
    '2026-09-30',
    '2025-08-10',
    '[
      {"fieldKey":"Organisation Name","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"Bob Smith Charity","timeStamp":"2025-08-10"}}},
      {"fieldKey":"Requested Amount (£)","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"60000","timeStamp":"2025-08-10"}}},
      {"fieldKey":"Number of Beneficiaries","type":"APPLICATION_SPECIFIC","currentVersion":1,"versions":{"1":{"value":"80","timeStamp":"2025-08-10"}}}
    ]',
    '[
      {"fieldKey":"Mission Statement","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We empower underserved communities through education and digital skills training.","timeStamp":"2025-08-10"}}},
      {"fieldKey":"Collaborative Partnerships","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We work closely with local councils, DWP job centres, Tech Nation, and a network of 30+ employer partners who provide mentors and job placements.","timeStamp":"2025-08-10"}}},
      {"fieldKey":"Measurement and Evaluation Approach","type":"PREDEFINED","currentVersion":1,"versions":{"1":{"value":"We track participant outcomes at 3, 6, and 12 months post-programme, measuring employment rates, income changes, and self-reported wellbeing scores.","timeStamp":"2025-08-10"}}}
    ]'
);

