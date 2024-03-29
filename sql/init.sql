DROP SCHEMA if exists SWT;

create schema if not exists SWT;

create table if not exists SWT.insurances
(
    id                          int auto_increment
        primary key,
    insurance_number            int not null unique,
    registration_date           date not null,
    insurance_number_expiration int null
);

create table if not exists SWT.pricing
(
    id                      int auto_increment
        primary key,
    purchase_date           date null,
    list_price_gross        int  null,
    leasing_installment_net int  null
);

create table if not exists SWT.users
(
    id        int auto_increment
        primary key,
    name      varchar(255) not null unique,
    password varchar(255) not null,
    role  varchar(8) check( role in ('admin', 'user', 'security')),
    is_driver tinyint(1) not null
);

create table if not exists SWT.access_groups
(
    id      int auto_increment
        primary key,
    user_id int          not null,
    `group` varchar(255) not null,
    constraint access_groups_user_fk
        foreign key (user_id) references users (id)
);

create table if not exists SWT.vehicles
(
    id                 int auto_increment
        primary key,
    license_plate      varchar(255) not null,
    brand              varchar(255) not null,
    model              varchar(255) not null,
    chassis_number     varchar(17)  not null,
    mileage            int          not null,
    expected_mileage   int          not null,
    annual_performance int          not null,
    insurance_id       int          not null,
    type               varchar(255) not null,
    pricing_id         int          null,
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

create table if not exists SWT.gas_cards
(
    id            int auto_increment
        primary key,
    number_aral   int          null,
    number_shell  int          null,
    pin           int          null,
    license_plate int          not null,
    holder        varchar(255) not null,
    vehicle_id    int          not null,
    constraint gas_cards_vehicle_fk
        foreign key (vehicle_id) references vehicles (id)
);
