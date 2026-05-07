CREATE TABLE charity (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  is_active BOOLEAN DEFAULT TRUE,
  created_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE APP_USERS (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL,
  charity_id BIGINT,
  is_active BOOLEAN DEFAULT TRUE,
  created_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE COMMON_DATA (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  charity_id BIGINT NOT NULL,
  version VARCHAR(255),
  data_json CLOB NOT NULL,
  template_title VARCHAR(255),
  description TEXT,
  is_active BOOLEAN DEFAULT TRUE,
  created_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE APPLICATION (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  charity_id BIGINT NOT NULL,
  application_number VARCHAR(255),
  project_name VARCHAR(255),
  funder_name VARCHAR(255),
  common_data_id BIGINT,
  selected_common_keys CLOB,
  application_data_json CLOB,
  comments TEXT,
  status VARCHAR(20) DEFAULT 'SUBMITTED',
  is_active BOOLEAN DEFAULT TRUE,
  created_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_by VARCHAR(100),
  modified_at TIMESTAMP
);