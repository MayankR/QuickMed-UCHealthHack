from django.db import models

# Create your models here.

class Hospitals(models.Model):
	name = models.CharField(max_length=50)
	username = models.CharField(max_length=50)
	password = models.CharField(max_length=50)
	latitude = models.DecimalField(max_digits=50,decimal_places=10)
	longitude = models.DecimalField(max_digits=50,decimal_places=10)

class Patients(models.Model):
	patient_id = models.AutoField(primary_key=True)
	name = models.CharField(max_length=50)

class PatientData(models.Model):
	data_id = models.AutoField(primary_key=True)
	patient_id = models.ForeignKey(Patients,on_delete=models.CASCADE)
	key = models.CharField(max_length=50)
	value = models.CharField(max_length=50)