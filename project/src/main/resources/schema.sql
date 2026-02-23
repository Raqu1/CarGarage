create TABLE IF NOT EXISTS cars (
    car_id    VARCHAR(10) PRIMARY KEY,
    car_brand VARCHAR(50)  NOT NULL,
    car_model VARCHAR(50)  NOT NULL,
    car_year  INT          NOT NULL,
    car_plate VARCHAR(20)  NOT NULL
);