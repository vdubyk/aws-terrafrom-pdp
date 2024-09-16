output "load_balancer_dns" {
  value       = aws_lb.app_lb.dns_name
  description = "DNS name for the application load balancer"
}

output "rds_endpoint" {
  value       = aws_db_instance.postgres.endpoint
  description = "Endpoint for the RDS instance"
}