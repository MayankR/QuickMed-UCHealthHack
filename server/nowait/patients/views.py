from django.shortcuts import render
from django.http import HttpResponse
from django.shortcuts import render
from .models import Hospitals, Patients
from django.shortcuts import redirect
import timeago, datetime
from django.http import JsonResponse

# Create your views here.

def index(response):
	return HttpResponse("Hello, world. You're at the polls index.")

def hospital_login(request):
	context = {"static_url_m": "/static/"}
	if request.method == "POST":
		username = request.POST.get("username", "")
		passwrd = request.POST.get("pass", "")
		results = Hospitals.objects.filter(username=username, password=passwrd)
		if len(results) == 1:
			response = redirect('/hospital/patients')
			response.set_cookie("loggedin", "yes");
			print(results[0])
			print(results[0].hospital_id)
			response.set_cookie("hospital_id", results[0].hospital_id)
			return response
		else:
			context["wrong_pswd"] = True
	else:
		is_loggedin = request.COOKIES.get('loggedin', '')
		if is_loggedin == "yes":
			return redirect('/hospital/patients')
	template_name = "hospital_login.html"
	return render(request, template_name, context)
	# return HttpResponse("Hello, world. You're at the polls index.")

def hospital_patients(request):
	is_loggedin = request.COOKIES.get('loggedin', '')
	if is_loggedin != "yes":
		return redirect('/hospital/login')

	hospital = Hospitals.objects.get(hospital_id=request.COOKIES.get('hospital_id', ''))

	patients = Patients.objects.filter(hospital=hospital)
	time_ago = []
	for patient in patients:
		now = datetime.datetime.now()# - datetime.timedelta(hours = 7)
		print(patient.name)
		print(now.strftime("%Y-%m-%d %H:%M"))
		added_date_time = datetime.datetime( patient.time_added.year,patient.time_added.month, patient.time_added.day, patient.time_added.hour,  patient.time_added.minute,  patient.time_added.second)
		print(added_date_time)
		
		time_ago.append(timeago.format(added_date_time, now))

	print(time_ago)
	context = {"static_url_m": "/static/", "hospital_name": hospital.name,
			"patients": patients, "time_ago": time_ago}
	template_name = "patient_list.html"
	return render(request, template_name, context)

def hospital_logout(request):
	response = redirect('/hospital/login')
	response.delete_cookie("loggedin")
	response.delete_cookie("hospital_id")

	return response

def add_patient_data(request):
	name = request.POST.get("name", "")
	age = request.POST.get("age", "")
	gender = request.POST.get("gender", "")
	problem = request.POST.get("problem", "")

	return JsonResponse({'patient_id':'22'})





