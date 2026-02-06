insert into roles (role)
values ('ROLE_TECHNICIAN'), ('ROLE_PURCHASER'), ('ROLE_MANAGER');

insert into users (username, password, created_by, modified_by, creation_date, modification_date)
values
    ('admin', '$2a$12$VhwuLkUhdcsasTZjOW9a5.94bjNKOgYQdBRnkb0tXyjah4TppfVBe', 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('test_manager', '$2a$12$wG9lTN/u/iZmH8nCjD2L5.K0Nl42INWLa8fhzB66B1Tium6vyzf5i', 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('test_technician', '$2a$12$wG9lTN/u/iZmH8nCjD2L5.K0Nl42INWLa8fhzB66B1Tium6vyzf5i', 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('test_purchaser', '$2a$12$wG9lTN/u/iZmH8nCjD2L5.K0Nl42INWLa8fhzB66B1Tium6vyzf5i', 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into contact_information (username, phone_number, business_email, created_by, modified_by, creation_date, modification_date)
values
    ('admin', '0612345678', 'admin@company.com', 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('test_manager', '0612345699', 'manager@company.com', 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('test_technician', '0612345677', 'technician@company.com', 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('test_purchaser', '0612345688', 'purchaser@company.com', 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into users_roles (users_username, roles_role)
values
    ('admin','ROLE_MANAGER'),
    ('admin','ROLE_TECHNICIAN'),
    ('admin','ROLE_PURCHASER'),
    ('test_manager','ROLE_MANAGER'),
    ('test_technician','ROLE_MANAGER'),
    ('test_purchaser','ROLE_MANAGER');

insert into equipment (id, name, total_maintenance_cost, total_maintenance_time, created_by, modified_by, creation_date, modification_date)
values
    (1,'Test machine 1', 0.00, 0, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2,'Test machine 2', 1240.00, 320, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
alter sequence equipment_seq restart with 100;

insert into parts (id, name, part_number, stock_quantity, unit_price, moving_average_price, reorder_point, reorder_quantity, created_by, modified_by, creation_date, modification_date)
values
    (1, 'Motor', '1236', 8, 480.00, 480.00, 4, 4, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Motor2', '12336', 5, 380.00, 380.00, 4, 4, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
alter sequence parts_seq restart with 100;

insert into work_orders (id, repair_time, is_open, total_cost_at_closure, equipment_id, created_by, modified_by, creation_date, modification_date)
values
    (1, 120, true, 0.00, 1, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 320, false, 1240.00, 2, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 230, true, 0.00, 1, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
alter sequence work_orders_seq restart with 100;

insert into work_order_line_items (id, quantity, received_quantity, total_issued_cost, status, part_id, work_order_id)
values
    (1, 1, 0, 0.00, 'OPEN', 1, 1),
    (2, 2, 0, 0.00, 'OPEN', 2, 1),
    (3, 1, 1, 480.00, 'CLOSED', 1, 2),
    (4, 2, 2, 760.00, 'CLOSED', 2, 2),
    (5, 2, 2, 960.00, 'CLOSED', 1, 3);
alter sequence work_order_line_items_seq restart with 100;

insert into purchase_orders (id, vendor_name, total_price, is_open, created_by, modified_by, creation_date, modification_date)
values
    (1, 'Banana Supplies', 480.00, true, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Apple Inc', 760.00, false, 'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
alter sequence purchase_orders_seq restart with 100;

insert into purchase_order_line_items (id, quantity, received_quantity, unit_price, delivery_date, delivery_status, part_id, purchase_order_id)
values
    (1, 1, 0, 480.00, '2026-03-20', 'OPEN', 1, 1),
    (2, 2, 2, 380.00, '2026-01-02', 'CLOSED', 2, 2);
alter sequence purchase_order_line_items_seq restart with 100;

insert into stock_movements (id, quantity, movement_type, purchase_order_id, work_order_id, part_id, created_by, modified_by, creation_date, modification_date)
values
    (1, 1, 'GOODS_RECEIPT', 1, null, 2,'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1, 'GOODS_RECEIPT', 1, null, 2,'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 1, 'GOODS_ISSUE', null, 2, 1,'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 2, 'GOODS_ISSUE', null, 2, 2,'SYSTEM', 'SYSTEM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
alter sequence stock_movements_seq restart with 100;
