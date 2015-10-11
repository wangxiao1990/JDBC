create database hw7;
use hw7;

create table Grouping(
  id int primary key auto_increment
);
create table Product(
  id int primary key auto_increment,
  weight double not null,
  volume double not null
);
create table Invoice(
  id int primary key,
  shippedOn date not null,
  foreign key(id) references Grouping(id) on update cascade on delete cascade
);
create table Box(
  id int primary key,
  maximumWeight double not null,
  volume double not null,
  fulfills int,
  foreign key(id) references Grouping(id) on update cascade on delete cascade,
  foreign key(fulfills) references Invoice(id) on update cascade on delete no action
);
create table Containment(
  contains int,
  isContainedIn int,
  count int not null,
  primary key(contains, isContainedIn),
  foreign key(contains) references Product(id) on update cascade on delete cascade,
  foreign key(isContainedIn) references Grouping(id) on update cascade on delete cascade
);
