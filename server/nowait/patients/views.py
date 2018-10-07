from django.shortcuts import render
from django.http import HttpResponse
from django.shortcuts import render
from .models import Hospitals
from django.shortcuts import redirect

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

	context = {"static_url_m": "/static/", "hospital_name": hospital.name}
	template_name = "patient_list.html"
	return render(request, template_name, context)

def hospital_logout(request):
	response = redirect('/hospital/login')
	response.delete_cookie("loggedin")
	response.delete_cookie("hospital_id")

	return response