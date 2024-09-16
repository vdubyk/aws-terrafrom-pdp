resource "aws_s3_bucket" "frontend" {
  bucket = "terraform-aws-java-pdp-frontend"
#  object_ownership = "ObjectWriter"
}

resource "aws_s3_bucket_website_configuration" "frontend_website" {
  bucket = aws_s3_bucket.frontend.id

  index_document {
    suffix = "index.html"
  }

  error_document {
    key = "error.html"
  }
}

resource "aws_s3_bucket_policy" "frontend_policy" {
  bucket = aws_s3_bucket.frontend.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect    = "Allow",
        Principal = "*",
        Action    = "s3:GetObject",
        Resource  = "${aws_s3_bucket.frontend.arn}/*"
      },
    ]
  })
}

resource "aws_s3_bucket_ownership_controls" "frontend_ownership_controls" {
  bucket = aws_s3_bucket.frontend.id

  rule {
    object_ownership = "ObjectWriter"
  }
}
