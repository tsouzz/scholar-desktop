-- ============================================================
-- English Course Student Management System
-- Database: MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS english_school
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE english_school;

-- ------------------------------------------------------------
-- Schools
-- ------------------------------------------------------------
CREATE TABLE schools (
  id    INT          NOT NULL AUTO_INCREMENT,
  name  VARCHAR(150) NOT NULL,
  PRIMARY KEY (id)
);

-- ------------------------------------------------------------
-- Teachers
-- ------------------------------------------------------------
CREATE TABLE teachers (
  id            INT          NOT NULL AUTO_INCREMENT,
  name          VARCHAR(150) NOT NULL,
  email         VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  school_id     INT          NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_teacher_school
    FOREIGN KEY (school_id) REFERENCES schools (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

-- ------------------------------------------------------------
-- Class Groups
-- The module column stores the Java enum constant name.
--   Ex: "INTERMEDIATE_1", "PRE_ADVANCED_2"
-- The name is derived in Java as:
--   name = module.getAcronym() + "-" + String.format("%04d", number)
--   Ex: "INT1-0001", "PADV2-0003"
-- ------------------------------------------------------------
CREATE TABLE class_groups (
  id          INT         NOT NULL AUTO_INCREMENT,
  number      INT         NOT NULL,
  module      VARCHAR(25) NOT NULL,
  teacher_id  INT         NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uq_class_group_module_number_teacher
    UNIQUE (module, number, teacher_id),
  CONSTRAINT fk_class_group_teacher
    FOREIGN KEY (teacher_id) REFERENCES teachers (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- Students
-- ------------------------------------------------------------
CREATE TABLE students (
  id              INT          NOT NULL AUTO_INCREMENT,
  name            VARCHAR(150) NOT NULL,
  class_group_id  INT          NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_student_class_group
    FOREIGN KEY (class_group_id) REFERENCES class_groups (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- Activities
-- Types and their weights:
--   AC_1, AC_2              ->  5% each  (10% total)
--   LC_1 ... LC_8           ->  1.25% each (10% total)
--   LW_1 ... LW_8           ->  1.25% each (10% total)
--   TO_MID                  -> 10%
--   TO_FINAL                -> 20%
--   TE_MID                  -> 20%
--   TE_FINAL                -> 20%
-- Maximum total: 100%
-- Each type is unique per student (uq_activity_student_type).
-- ------------------------------------------------------------
CREATE TABLE activities (
  id                INT          NOT NULL AUTO_INCREMENT,
  student_id        INT          NOT NULL,
  type              ENUM(
                      'AC_1','AC_2',
                      'LC_1','LC_2','LC_3','LC_4','LC_5','LC_6','LC_7','LC_8',
                      'LW_1','LW_2','LW_3','LW_4','LW_5','LW_6','LW_7','LW_8',
                      'TO_MID','TO_FINAL',
                      'TE_MID','TE_FINAL'
                    ) NOT NULL,
  grade             DECIMAL(5,2) NOT NULL,
  registration_date DATE         NOT NULL DEFAULT (CURRENT_DATE),
  PRIMARY KEY (id),
  CONSTRAINT uq_activity_student_type
    UNIQUE (student_id, type),
  CONSTRAINT chk_grade
    CHECK (grade >= 0 AND grade <= 10),
  CONSTRAINT fk_activity_student
    FOREIGN KEY (student_id) REFERENCES students (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);
