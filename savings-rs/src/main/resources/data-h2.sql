
INSERT INTO users (id, username, password) VALUES (1, 'john', 'doe');

INSERT INTO users (id, username, password) VALUES (2, 'jane', 'doe');

INSERT INTO savings_accounts (id, iban, description, user_id)
    VALUES (1, 'iban1', 'My savings', 2);
