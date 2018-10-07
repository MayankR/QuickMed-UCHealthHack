from django.shortcuts import render
from django.http import HttpResponse
from django.shortcuts import render
from .models import Hospitals

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
			return HttpResponse("Loggedin")
		else:
			context["wrong_pswd"] = True 
	template_name = "hospital_login.html"
	return render(request, template_name, context)
	# return HttpResponse("Hello, world. You're at the polls index.")