insert into member(id, username, password, roles) values
(1, 'admin', '$2a$10$8AdjoX7mnMu2rMy2BE8oU.oMxT6zYna3GoYdr6gNfBEkhA9lyHkF2', json_array('ROLE_READ', 'ROLE_WRITE'));

insert into company(id, name, ticker) values
(1, 'NVIDIA Corporation', 'NVDA'),
(2, 'GameStop', 'GME');

insert into dividend(id, company_id, date, dividend) values
(1, 1, '2024-01-10', '0.04'),
(2, 1, '2024-01-09', '0.0375'),
(3, 1, '2024-01-08', '0.04'),
(4, 1, '2024-01-07', '0.04'),
(5, 1, '2024-01-06', '0.04'),
(6, 1, '2024-01-05', '0.04'),
(7, 1, '2024-01-04', '0.04'),
(8, 1, '2024-01-03', '0.04'),
(9, 1, '2024-01-02', '0.04'),
(10, 1, '2024-01-01', '0.04');
