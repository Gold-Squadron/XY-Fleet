use SWT;
INSERT INTO  users (name, password, role, is_driver)
VALUES ('lhelbig', '123', 'admin', '1'),
       ('nsimon', '123', 'admin', '1'),
       ('jwilleke', '123', 'admin', '1'),
       ('laußem', '123', 'admin', '1'),
       ('user', '123', 'user', '0'),
       ('security', '123', 'security', '0');
INSERT INTO users (id, name, password, is_driver)
VALUES
       (101,'askywalker', 'abc', '1'),
       (102,'dvader', 'abc', '1'),
       (103,'hsolo', 'abc', '1'),
       (104, 'okenobi', 'abc', '1'),
       (105, 'myoda', 'abc', '1'),
       (106, 'mwindu', 'abc', '1');
INSERT INTO insurances
VALUES (100,100234992, 20050101, 20300202),
       (101,496652100, 20100304, 20280309),
       (102,900625487, 20150630, 20401212),
       (103,442019930, 20020318, 20250210),
       (104,988930112, 20090411, 20281111),
       (105,999222818, 20071031, 20250820),
       (106,400382122, 20080707, 20250606);
INSERT INTO pricing
VALUES (100,'2014-10-13', 100000, 250),
       (101,'2020-02-02', 250000, 400),
       (102,'2023-10-10', 300000, 100),
       (103,'2024-01-01', 1000000, 1000),
       (104, '2022-12-01', 150000, 300),
       (105, '2022-08-31', 605000, 400),
        (106, '2021-01-05', 575999, 299);
INSERT INTO vehicles
VALUES (100, 'STO-XY-1', 'CITROEN', 'C2', '1G1YY25R69570001', 30000, 34000, 5000, 100, 'car', 100),
       (101, 'STO-XY-2', 'VW', 'Golf GTI', '2F4XX22D3089661', 10000, 11000, 3000, 101, 'car', 101),
       (102, 'STO-XY-3', 'VW', 'Tiguan', '3H4ZZ11E991833262', 1000, 2000, 10000, 102, 'car', 102),
       (103, 'STO-XY-4', 'Mercedes', 'AMG-GT', '9H3BD76L3044292', 0, 500, 2000, 103, 'car', 103),
       (104, 'STO-XY-5', 'Toyota', 'Mustang', '3G2EE25I692877166', 4000, 5000, 3000, 104, 'car', 104),
       (105, 'STO-XY-6', 'Ford', 'Ranger', '5A5FF9918N99105', 150000, 155000, 10000, 105, 'car', 105),
       (106, 'STO-XY-7', 'Tesla', 'Cybertruck', '2G2UU8271N7273', 2000, 8000, 12000, 106, 'car', 106),
       (107, 'STO-XY-8', 'Tesla', 'Cybertruck', '2G2UU8271N7273', 2000, 8000, 12000, 106, 'car', 106);
INSERT INTO bookings (driver_id, vehicle_id, leasing_start, leasing_end, reasoning, expected_travel_distance)
VALUES (101, 100, '2024-03-01', '2024-03-07', 'Betriebsausflug', 1000),
       (102, 100, '2024-03-14', '2024-03-15', 'none', 200),
       (101, 100, '2024-03-20', '2024-03-30', 'Betriebsreise', 2800),
       (103, 101, '2024-03-05', '2024-03-05', 'none', 100),
       (103, 101, '2024-03-12', '2024-03-12', 'none', 100),
       (103, 101, '2024-03-19', '2024-03-19', 'none', 100),
       (103, 101, '2024-03-26', '2024-03-26', 'none', 100),
       (102, 101, '2024-03-20', '2024-03-24', 'Betriebsausflug', 600),
       (102, 102, '2024-03-10','2024-03-12','Deutschlandtour', 1000),
       (101, 103, '2024-03-11', '2024-03-13', 'none', 200),
       (102, 103, '2024-03-20', '2024-03-25', 'none', 300),
       (104, 104, '2024-03-03', '2024-03-04', 'none', 200),
       (104, 104, '2024-03-10', '2024-03-10', 'none', 100),
       (104, 104, '2024-03-28', '2024-03-30', 'none', 600),
       (105, 105, '2024-03-20', '2024-04-10', 'none', 4000),
       (104, 105, '2024-03-08', '2024-03-12', 'none', 1000),
       (106, 106, '2024-02-20', '2024-03-12', 'Spritztour', 6000);
INSERT INTO bookings(driver_id, vehicle_id, leasing_start, leasing_end, reasoning, expected_travel_distance, status)
VALUES (null, 107, '2024-03-01', '2024-03-15', 'none', 200, 'maintenance');