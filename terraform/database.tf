resource "aws_db_subnet_group" "default" {
  name        = "rds-subnet-group"
  subnet_ids  = [aws_subnet.public.id, aws_subnet.public_additional.id]
  description = "Subnet group for RDS instance"
}

resource "aws_db_instance" "postgres" {
  allocated_storage       = 20
  engine                  = "postgres"
  engine_version          = "16.3"
  instance_class          = "db.t3.micro"
  db_name                 = "awspdpdb"
  username                = data.aws_ssm_parameter.db_username.value
  password                = data.aws_ssm_parameter.db_password.value
  publicly_accessible     = false
  vpc_security_group_ids  = [aws_security_group.rds_sg.id]
  skip_final_snapshot     = true
  db_subnet_group_name    = aws_db_subnet_group.default.name
}