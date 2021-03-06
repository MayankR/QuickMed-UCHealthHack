"""nowait URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from patients.views import index, hospital_login, hospital_patients
from patients.views import hospital_logout, add_patient_data
from patients.views import view_patient_data

urlpatterns = [
	path('', index, name='index'),
    path('hospital/login', hospital_login, name='index'),
    path('hospital/logout', hospital_logout, name='index'),
    path('hospital/patients', hospital_patients, name='index'),
    path('addPatientData', add_patient_data, name='index'),
    path('hospital/patientdata/<int:pid>', view_patient_data, name='index'),

    path('admin/', admin.site.urls),
]
