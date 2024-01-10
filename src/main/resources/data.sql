insert into member(username, password, roles) values
('admin', '$2a$10$8AdjoX7mnMu2rMy2BE8oU.oMxT6zYna3GoYdr6gNfBEkhA9lyHkF2', json_array('ROLE_READ', 'ROLE_WRITE'));

insert into company(name, ticker) values
('NVIDIA Corporation', 'NVDA'),
('GameStop', 'GME');

insert into dividend(company_id, date, dividend) values
(1, '2024-01-10', '0.04'),
(1, '2024-01-09', '0.0375'),
(1, '2024-01-08', '0.04'),
(1, '2024-01-07', '0.04'),
(1, '2024-01-06', '0.04'),
(1, '2024-01-05', '0.04'),
(1, '2024-01-04', '0.04'),
(1, '2024-01-03', '0.04'),
(1, '2024-01-02', '0.04'),
(1, '2024-01-01', '0.04');
