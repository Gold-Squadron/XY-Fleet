DROP SCHEMA if exists SWT;

create schema if not exists SWT;

create table if not exists SWT.insurances
(
    id                          int auto_increment
        primary key,
    insurance_number            int not null unique,
    registration_date           date not null,
    insurance_number_expiration_date date not null
);

create table if not exists SWT.pricing
(
    id                      int auto_increment
        primary key,
    purchase_date           date null,
    list_price_gross        int  not null,
    leasing_installment_net int  not null,
    leasing_start           date not null,
    leasing_end             date not null
);

create table if not exists SWT.users
(
    id        int auto_increment
        primary key,
    name      varchar(255) not null unique,
    password varchar(255) not null,
    role  varchar(8)  default(null) check( role in ('admin', 'user', 'security', null)),
    is_driver tinyint(1) not null
);

create table if not exists SWT.access_groups
(
    id      int auto_increment
        primary key,
    user_id int,
    `group` varchar(255) not null unique,
    is_bookable boolean not null,
    constraint access_groups_user_fk
        foreign key (user_id) references users (id)
);
create table if not exists SWT.fuel_card
(
    id                int auto_increment
        primary key ,
    aral numeric(17) unique,
    shell numeric(17) unique,
    pin int
);
create table if not exists SWT.vehicles
(
    id                 int auto_increment
        primary key,
    license_plate      varchar(255) not null unique,
    brand              varchar(255) not null,
    model              varchar(255) not null,
    chassis_number     varchar(17)  not null,
    mileage            int          not null,
    expected_mileage   int          not null,
    annual_performance int          not null,
    insurance_id       int          not null,
    type               varchar(255) not null,
    pricing_id         int          not null,
    fuel_card_id       int          not null,
    access_group_id    int          not null,
    seats              int          default(2)  check(seats>0),
    constraint vehicles_access_group_fk
        foreign key (access_group_id) references access_groups (id),
    constraint vehicles_fuel_card_fk
        foreign key (fuel_card_id) references fuel_card (id),
    constraint vehicles_insurance_fk
        foreign key (insurance_id) references insurances (id),
    constraint vehicles_pricing_fk
        foreign key (pricing_id) references pricing (id)
);

create table if not exists SWT.bookings
(
    id            int auto_increment
        primary key,
    driver_id     int          check((status IS NULL and driver_id IS NOT NULL) or (status IS NOT NULL and driver_id IS NULL)),
    vehicle_id    int          not null,
    leasing_start date         not null,
    leasing_end   date         not null,
    reasoning     varchar(255) not null,
    expected_travel_distance int not null check(expected_travel_distance>0),
    status varchar(11)  check(status in('maintenance', 'defective', 'repair', 'other', null)),
    constraint bookings_driver_fk
        foreign key (driver_id) references users (id),
    constraint bookings_vehicle_fk
        foreign key (vehicle_id) references vehicles (id)
);
create table if not exists SWT.tokens
(
    id int auto_increment primary key,
    token varchar(50) not null,
    user_name varchar(255) not null unique,
    constraint tokens_users_fk
        foreign key(user_name) references users(name)
);
create table if not exists SWT.settings
(
    id int auto_increment primary key,
    `key` varchar(255) not null,
    `value` varchar(255),
    user_id int not null,
    constraint settings_users_fk
        foreign key(user_id) references users(id)
);