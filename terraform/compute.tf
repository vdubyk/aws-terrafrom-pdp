data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

data "aws_ssm_parameter" "db_username" {
  name = "/db/username"
}

data "aws_ssm_parameter" "db_password" {
  name = "/db/password"
}

resource "aws_launch_template" "backend_template" {
  name_prefix   = "backend-template"
  image_id      = data.aws_ami.ubuntu.id
  instance_type = "t2.micro"

  iam_instance_profile {
    name = aws_iam_instance_profile.instance_profile.name
  }

  block_device_mappings {
    device_name = "/dev/xvda"
    ebs {
      volume_size = 20
    }
  }

  network_interfaces {
    associate_public_ip_address = true
    security_groups             = [aws_security_group.backend_sg.id]
  }

  user_data = base64encode(templatefile("${path.module}/user_data.sh.tpl", {
    db_username = data.aws_ssm_parameter.db_username.value
    db_password = data.aws_ssm_parameter.db_password.value
    db_url = "jdbc:postgresql://${aws_db_instance.postgres.endpoint}/${aws_db_instance.postgres.db_name}"
    origin_url  = "http://${aws_s3_bucket.frontend.bucket}.s3-website-${var.region}.amazonaws.com"
  }))
}

resource "aws_autoscaling_group" "backend_asg" {
  desired_capacity     = 1
  max_size             = 3
  min_size             = 1
  vpc_zone_identifier  = [aws_subnet.public.id]
  target_group_arns    = [aws_lb_target_group.app_tg.arn]
  launch_template {
    id = aws_launch_template.backend_template.id
  }
  health_check_type         = "EC2"
  health_check_grace_period = 300
  depends_on                = [
    aws_db_instance.postgres,
    data.aws_ssm_parameter.db_username,
    data.aws_ssm_parameter.db_password
  ]
}

resource "aws_ssm_parameter" "asg_name" {
  name  = "/myapp/backend-asg-name"
  type  = "String"
  value = aws_autoscaling_group.backend_asg.name
}
