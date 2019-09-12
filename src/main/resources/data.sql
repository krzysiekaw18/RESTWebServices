INSERT INTO student VALUES (1, DATE '1998-12-15', 'o.healey@gmail.com', 'Ozell', 'Healey', 2)
INSERT INTO student VALUES (2, DATE '1997-06-17', 'l.phegley@gmail.com', 'Lauri', 'Phegley', 3)
INSERT INTO student VALUES (3, DATE '1999-04-02', 'j.barron@gmail.com', 'Juan', 'Barron', 1)
INSERT INTO student VALUES (4, DATE '1998-10-05', 's.daum@gmail.com', 'Shirley', 'Daum', 2)
INSERT INTO student VALUES (5, DATE '1995-01-20', 'j.mcclaine@gmail.com', 'Juanita', 'Mcclaine', 5)
INSERT INTO student VALUES (6, DATE '1995-11-29', 'j.gallivan@gmail.com', 'Josue', 'Gallivan', 5)
INSERT INTO student VALUES (7, DATE '1999-03-30', 't.offutt@gmail.com', 'Trinity', 'Offutt', 1)
INSERT INTO student VALUES (8, DATE '1998-09-10', 'm.doner@gmail.com', 'Mila', 'Doner', 2)
INSERT INTO student VALUES (9, DATE '1995-02-28', 'f.difiore@gmail.com', 'Floria', 'Difiore', 5)
INSERT INTO student VALUES (10, DATE '1998-05-13', 'g.bermea@gmail.com', 'Gracie', 'Bermea', 2)

INSERT INTO subject VALUES (1, 'Maths')
INSERT INTO subject VALUES (2, 'Biology')
INSERT INTO subject VALUES (3, 'History')
INSERT INTO subject VALUES (4, 'Geography')
INSERT INTO subject VALUES (5, 'Physics')
INSERT INTO subject VALUES (6, 'English')
INSERT INTO subject VALUES (7, 'French')
INSERT INTO subject VALUES (8, 'Chemistry')

INSERT INTO student_subject_link VALUES (1, 1)
INSERT INTO student_subject_link VALUES (1, 5)
INSERT INTO student_subject_link VALUES (1, 6)
INSERT INTO student_subject_link VALUES (1, 7)
INSERT INTO student_subject_link VALUES (2, 2)
INSERT INTO student_subject_link VALUES (2, 3)
INSERT INTO student_subject_link VALUES (3, 1)
INSERT INTO student_subject_link VALUES (3, 5)
INSERT INTO student_subject_link VALUES (3, 8)
INSERT INTO student_subject_link VALUES (4, 7)
INSERT INTO student_subject_link VALUES (5, 4)
INSERT INTO student_subject_link VALUES (5, 5)
INSERT INTO student_subject_link VALUES (6, 1)
INSERT INTO student_subject_link VALUES (6, 3)
INSERT INTO student_subject_link VALUES (6, 6)
INSERT INTO student_subject_link VALUES (7, 2)
INSERT INTO student_subject_link VALUES (7, 4)
INSERT INTO student_subject_link VALUES (8, 1)
INSERT INTO student_subject_link VALUES (9, 2)
INSERT INTO student_subject_link VALUES (9, 4)
INSERT INTO student_subject_link VALUES (9, 6)
INSERT INTO student_subject_link VALUES (9, 8)
INSERT INTO student_subject_link VALUES (10, 3)
INSERT INTO student_subject_link VALUES (10, 6)