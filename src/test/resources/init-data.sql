TRUNCATE TABLE client_actions;
TRUNCATE TABLE clients;

INSERT INTO clients (id, name, surname, email, password, balance) VALUES 
(1, 'Dirk', 'Kuyt', 'dirk@yahoo.com', '1111', 1500);

INSERT INTO client_actions (id, bank_client_id,amount,type) VALUES
(1, 1, 2000, 'DEPOSIT')

INSERT INTO client_actions (id, bank_client_id,amount,type) VALUES
(2, 1, 500, 'WITHDRAW')