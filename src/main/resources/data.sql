insert ignore into `application` (application_id, name, student_number, major, email, phone_number, tech_stack, application_status, is_opened, is_marked, track)
values (1, '철수', '2018001', '컴퓨터 사이언스', 'email1@email.com', 010-0000-0000, 'Java, Spring', 'TEMPORAL', false, false, 'BACK_END'),
       (2, '영희', '2018002', '컴퓨터 사이언스', 'email2@email.com', 010-0000-0001, 'Java, Spring', 'TEMPORAL', false, false, 'BACK_END'),
       (12, '철희', '2018007', '컴퓨터 사이언스', 'email7@email.com', 010-0000-0007, 'Java, Spring', 'TEMPORAL', false, false, 'FRONT_END'),
       (13, '영수', '2018008', '컴퓨터 사이언스', 'email8@email.com', 010-0000-0008, 'Java, Spring', 'TEMPORAL', false, false, 'FRONT_END'),
       (3, '민수', '2018003', '컴퓨터 사이언스', 'email3@email.com', 010-0000-0002, 'Java, Spring', 'SAVED', true, false, 'BACK_END'),
       (4, '지수', '2018004', '컴퓨터 사이언스', 'email4@email.com', 010-0000-0003, 'Java, Spring', 'SAVED', true, false, 'BACK_END'),
       (5, '영수', '2018005', '컴퓨터 사이언스', 'email5@email.com', 010-0000-0004, 'Java, Spring', 'SAVED', true, false, 'BACK_END'),
       (6, '민지', '2018006', '컴퓨터 사이언스', 'email6@email.com', 010-0000-0005, 'Java, Spring', 'SAVED', true, false, 'BACK_END'),
       (7, '철수', '2018009', '컴퓨터 사이언스', 'email9@email.com', 010-0000-0009, 'Java, Spring', 'SAVED', true, true, 'BACK_END'),
       (8, '영희', '2018010', '컴퓨터 사이언스', 'email10@email.com', 010-0000-0010, 'Java, Spring', 'SAVED', true, true, 'BACK_END'),
       (9, '민수', '2018011', '컴퓨터 사이언스', 'email11@email.com', 010-0000-0011, 'Java, Spring', 'REJECTED', true, false, 'BACK_END'),
       (10, '지수', '2018012', '컴퓨터 사이언스', 'email12@email.com', 010-0000-0012, 'Java, Spring', 'APPROVED', true, true, 'BACK_END'),
       (11, '영수', '2018013', '컴퓨터 사이언스', 'email13@email.com', 010-0000-0013, 'Java, Spring', 'APPROVED', true, true, 'BACK_END');