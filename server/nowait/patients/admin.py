from django.contrib import admin
from .models import Hospitals, Patients, PatientData

# Register your models here.
admin.site.register(Hospitals)
admin.site.register(Patients)
admin.site.register(PatientData)