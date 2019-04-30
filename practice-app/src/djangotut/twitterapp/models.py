from django.db import models

# Create your models here.

# random model, you can create new ones as an exercise
class Article(models.Model):
    title = models.CharField(max_length=120)
    content = models.TextField()
    active = models.BooleanField(default=True)