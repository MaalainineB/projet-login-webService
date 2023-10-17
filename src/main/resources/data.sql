INSERT INTO user_info
VALUES (1,'Mark','mark@email.com', '$2a$10$DnscJPan5ASMrAZCdN4mD.AHk5D2finfXTIXLqWjrS8xBobo9x04e','ROLE_USER');

SELECT setval('user_info_id_seq', 2, false);
