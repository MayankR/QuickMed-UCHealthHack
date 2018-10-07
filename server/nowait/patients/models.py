from django.db import models

# Create your models here.

class Hospitals(models.Model):
	hospital_id = models.AutoField(primary_key=True)
	name = models.CharField(max_length=50)
	username = models.CharField(max_length=50)
	password = models.CharField(max_length=50)
	latitude = models.DecimalField(max_digits=50,decimal_places=10)
	longitude = models.DecimalField(max_digits=50,decimal_places=10)

	def __str__ (self):
		return self.name

class Patients(models.Model):
	patient_id = models.AutoField(primary_key=True)
	name = models.CharField(max_length=50)
	age = models.CharField(max_length=6)
	gender = models.CharField(max_length=10)
	whats_wrong = models.CharField(max_length=200)
	hospital = models.ForeignKey(Hospitals,on_delete=models.CASCADE)
	time_added = models.DateTimeField(auto_now=True)

	def __str__ (self):
		return self.name

class PatientData(models.Model):
	data_id = models.AutoField(primary_key=True)
	patient_id = models.ForeignKey(Patients,on_delete=models.CASCADE)
	key = models.CharField(max_length=50)
	value = models.CharField(max_length=50)

	def __str__ (self):
		return "%s: %s" % (self.key, self.value)