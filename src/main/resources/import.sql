insert into users values (1, '$2a$10$IE4XZPWC.BfLtTIqD5rZ0ewiF8YKIKVKIldHOhNgsASdKn7fu4o.G', 'ADMIN', 'admin');
insert into users values (2, '123456', 'CUSTOMER', 'petea');
insert into users values (3, '123456', 'COMPANY', 'orange');
insert into users values (4, '123456', 'Customer', 'vova');
INSERT INTO users VALUES (5, '123456', 'company', 'cvas');

insert into company VALUES (1, '444444444', 'comp1@gmail.com', 'company1', 3);
insert into company VALUES (2, '333333333', 'comp2@gmail.com', 'company2', 3);
insert into company VALUES (3, '00000000011111', 'cvasprom@gmail.com', 'CvasPromRussia', 5);

insert into customer VALUES (1, '55555555', 'petea@gmail.com', 'petr', 'pupkin', 2);
insert into customer VALUES (2, '11111222222200', 'vova@gmail.com', 'vladimir', 'ivanov', 4);

insert into service VALUES (1, 'GSM service Chisinau', 'GSM service', 100.50);
insert into service VALUES (2, 'Apacanal not-service Moldova', 'Apacanal', 25.50);
insert into service VALUES (3, 'Gas service Gazprom', 'Gas', 10000.59);

INSERT INTO company_services VALUES (1, 1);
INSERT INTO company_services VALUES (2, 2);
INSERT INTO company_services VALUES (3, 3);
INSERT INTO company_services VALUES (2, 1);

INSERT INTO contract (id, sum, company_id, customer_id, service_id) VALUES (1, 100000, 1, 1, 1);
INSERT INTO contract (id, sum, company_id, customer_id, service_id) VALUES (2, 30000, 1, 2, 2);
INSERT INTO contract (id, sum, company_id, customer_id, service_id) VALUES (3, 1000000, 3, 2, 3);

INSERT INTO invoice (id, status, sum, contract_id) VALUES (1, 'NOTPAID', 50.45, 1);
INSERT INTO invoice (id, status, sum, contract_id) VALUES (2, 'NOTPAID', 115.54, 2);
INSERT INTO invoice (id, status, sum, contract_id) VALUES (3, 'PAID', 505.58, 3);



