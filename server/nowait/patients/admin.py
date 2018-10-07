from django.contrib import admin
from .models import Hospitals, Patients, PatientData

class PatientsAdmin(admin.ModelAdmin):
    readonly_fields = ('time_added',)

# Register your models here.
admin.site.register(Hospitals)
admin.site.register(Patients, PatientsAdmin)
admin.site.register(PatientData)