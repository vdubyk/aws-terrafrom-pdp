terraform {
  backend "s3" {
    bucket = "terraform-aws-java-pdp"
    key = "terraform"
    region = "eu-west-1"
  }
}