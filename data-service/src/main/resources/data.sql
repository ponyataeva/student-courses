INSERT INTO user (id, login, password) VALUES (1, 'user_1', 'test_1');
INSERT INTO user (id, login, password) VALUES (2, 'user_2', 'test_2');
INSERT INTO user (id, login, password) VALUES (3, 'user_3', 'test_3');

INSERT INTO course (name, grade) VALUES ('course_A', 'A');
INSERT INTO course (id, name, grade) VALUES (2, 'course_B', 'B');
INSERT INTO course (id, name, grade) VALUES (3, 'course_F', 'F');
INSERT INTO course (id, name, grade) VALUES (4, 'course_D', 'D');
INSERT INTO course (id, name, grade) VALUES (5, 'course_Z', 'Z');

INSERT INTO userinfo (USER_ID) VALUES (1);
INSERT INTO userinfo (USER_ID) VALUES (2);
INSERT INTO userinfo (USER_ID) VALUES (3);

INSERT INTO user_course (user_id, course_id) VALUES (1, 1);
INSERT INTO user_course (user_id, course_id) VALUES (2, 1);
INSERT INTO user_course (user_id, course_id) VALUES (1, 2);
INSERT INTO user_course (user_id, course_id) VALUES (3, 2);
INSERT INTO user_course (user_id, course_id) VALUES (3, 3);





