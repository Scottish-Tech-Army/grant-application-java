INSERT INTO charity (id,name, created_by)
VALUES
(1,'Helping Hands Trust', 'admin'),
(2,'Global Aid Foundation', 'admin'),
(3,'Sunshine Charity', 'admin');

INSERT INTO APP_USERS (username, password, role, charity_id, created_by)
VALUES
('admin', '$2a$10$RPlvVGqhC5vrRIwM9TbN5eOEL9F9w.WUo7BhTMhJ6Gfl75sxbnjEG', 'ADMIN', 1, 'admin'), --admin123
('user', '$2a$10$miasSC6U3.6QXcAT2ALiwOUdITfzCgNzmyoZbX9efV6oechaMkCam', 'USER', 1, 'user'), --user123
('admin2', '$2a$10$W/am/HLfe4qVKUmH1N/KcubbGdKBuBexJfvMG.TUa62VV9UtYzQgS', 'ADMIN', 2, 'admin'), --admin123
('user2', '$2a$10$.hBTcebdX74Bjuui9iNwLe3NXElS37/Nu.8DjeYYKvmnGWYZS0kpG', 'USER', 2, 'user'), --user123
('admin3', '$2a$10$anCMesP9A2l4lfig59AY6OQFnZfzCWDHMxs9AfZAkWyA3zZMaf49S', 'ADMIN', 3, 'admin'), --admin123
('user3', '$2a$10$9PJ.pw0TG15dU8mzjtPLm.0WlKU3dPvhPD3qv0iURqMwYJglg8djW', 'USER', 3, 'user'); --user123

INSERT INTO COMMON_DATA (charity_id, version, data_json, created_by, template_title, description)
VALUES (
  1,
  'v2026-02-13T05:09:35',
  '[{"key":"Charity name","value":"Helping Hands","comments":"","id":1},{"key":"Legal status","value":"Trust ","comments":"","id":2},{"key":"Registration number","value":"ABCD1234","comments":"Please find below link for registration doc.\nhttps://helpinghandf.org/","id":3},{"key":"Year of registration","value":"1990","comments":"Please find below link for registration doc.\nhttps://helpinghandf.org/","id":4},{"key":"Registered address","value":"Helping Hands Trust\nHelping Hands House\n12 Community Way\nGreenfield Park\nLondon\nGreater London\nSW1A 1AA","comments":"","id":5},{"key":"Website / social media links","value":"instagram : @helpinghands\nwww.helpinghandstrust.com","comments":"","id":6},{"key":"Trustees / Board members","value":"Arun (chairman)\n Tanya (Chief Secretory)\n","comments":"","id":7},{"key":"Vision ","value":"A compassionate and inclusive society where every individual has access to basic needs, dignity, and equal opportunities to thrive.","comments":"","id":8}]',
  'admin',
  'Helping Hand Charity Data',
  'Template for basic organization information used in applications.'
);

INSERT INTO COMMON_DATA
  (charity_id, version, data_json, template_title, description, is_active, created_by, created_at)
VALUES
  (
    1,
    'v2026-03-31T05:09:40',
    '[{"key":"Charity name","value":"Helping Hands","comments":"","id":1},{"key":"Legal status","value":"Trust ","comments":"","id":2},{"key":"Registration number","value":"ABCD1234","comments":"Please find below link for registration doc.\nhttps://helpinghandf.org/","id":3},{"key":"Year of registration","value":"1990","comments":"Please find below link for registration doc.\nhttps://helpinghandf.org/","id":4},{"key":"Registered address","value":"Helping Hands Trust\nHelping Hands House\n12 Community Way\nGreenfield Park\nLondon\nGreater London\nSW1A 1AA","comments":"","id":5},{"key":"Website / social media links","value":"instagram : @helpinghands\nwww.helpinghandstrust.com","comments":"","id":6},{"key":"Trustees / Board members","value":"Nikilesh (chairman)\nAnwar (Chief Secretory)\n","comments":"","id":7},{"key":"Vision ","value":"A compassionate and inclusive society where every individual has access to basic needs, dignity, and equal opportunities to thrive.","comments":"","id":8}]',
    'Helping Hand Charity template',
    'Common Org data including Name, Contact info, ',
    TRUE,
    'admin',
    TIMESTAMP '2026-03-31 10:39:40.412802'
  );

INSERT INTO application (
     charity_id,
     application_number,
     project_name,
     funder_name,
     common_data_id,
     selected_common_keys,
     application_data_json,
     comments,
     status,
     is_active,
     created_by,
     created_at,
     modified_by,
     modified_at
)
VALUES (
  1,
  'APP-20260331105118',
  'Child Support Project',
  'ABC Foundation',
  1,
  '[1,2,3,4,5,6,7,8]',
  '[{"key":"Total project cost","value":"10000(EUR)","comments":"This is approximate cost"},
        {"key":"Requested Amount ","value":"4000(EUR)"},
        {"key":"Number of childs to be supported from Project","value":"500"}
        ]',
  'Initial draft submitted. Needs review before funding decision.',
  'DRAFT',
     TRUE,
     'admin',
     TIMESTAMP '2026-02-13 10:52:28.752552',
     'admin',
     TIMESTAMP '2026-02-13 10:52:28.752552'
);


INSERT INTO APPLICATION
  (charity_id,
   application_number,
   project_name,
   funder_name,
   common_data_id,
   selected_common_keys,
   application_data_json,
   comments,
   status,
   is_active,
   created_by,
   created_at,
   modified_by,
   modified_at)
VALUES
  (
   1,
   'APP-20260331105228',
   'Bright Futures Education Program',
   'Lloyds Bank',
   2,
   '[1,2,3,4,5,6,7,8]',
   '[{"key":"Total project cost","value":"50000(EUR)","comments":"This is approximate cost"},
     {"key":"Requested Amount ","value":"20000(EUR)"},
     {"key":"Budget Summary ","value":"The total cost of the Bright Futures Education Program is EUR 50,000, covering educational materials, qualified teaching support, student assistance, learning infrastructure, project management, and monitoring to ensure effective delivery and measurable outcomes."},
     {"key":"Number of students to be supported from Project","value":"500"},
     {"key":"Education Problem Being Addressed","value":"Children from economically disadvantaged communities face significant barriers to accessing quality education. Many students struggle with learning gaps due to irregular school attendance, lack of academic support at home, inadequate learning materials, and limited access to trained teachers. As a result, students fall behind grade‑level expectations, leading to poor academic performance and increased dropout rates."}]',
   'Initial draft submitted. Needs review before funding decision.',
   'DRAFT',
   TRUE,
   'admin',
   TIMESTAMP '2026-03-31 10:52:28.752552',
   'admin',
   TIMESTAMP '2026-03-31 10:52:28.752552'
  );
