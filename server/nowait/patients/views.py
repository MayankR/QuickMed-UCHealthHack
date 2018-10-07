from django.shortcuts import render
from django.http import HttpResponse
from django.shortcuts import render
from .models import Hospitals, Patients, PatientData
from django.shortcuts import redirect
import timeago, datetime
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

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
		now = datetime.datetime.now()
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

@csrf_exempt
def add_patient_data(request):
	name = request.POST.get("name", "")
	age = request.POST.get("age", "")
	gender = request.POST.get("gender", "")
	img1 = request.POST.get("img1", "")
	img2 = request.POST.get("img2", "")
	problem = request.POST.get("problem", "-")
	hospital_id = request.POST.get("hospital_id", "1")
	print("got data")
	print(img1[:30])
	print(img2[:30])

	hospital = Hospitals.objects.get(hospital_id=hospital_id)
	p = Patients(name=name, age=age, gender=gender, whats_wrong=problem, hospital=hospital,img1=img1,img2=img2)
	p.save()

	for k in request.POST:
		if k[:4] == "key_":
			v = request.POST[k]
			pd = PatientData(patient_id=p, key=k[4:], value=v)
			pd.save()

	return JsonResponse({'patient_id':'22'})

def view_patient_data(request, pid):
	p = Patients.objects.get(patient_id=pid)
	pdata = PatientData.objects.filter(patient_id=p)

	now = datetime.datetime.now()
	added_date_time = datetime.datetime( p.time_added.year,p.time_added.month, p.time_added.day, p.time_added.hour,  p.time_added.minute,  p.time_added.second)	
	t_ago_str = timeago.format(added_date_time, now)

	context = {"static_url_m": "/static/", "hospital_name": p.hospital.name,
		"patient": p, "pdata": pdata, "t_ago": t_ago_str}
	template_name = "patient_detail.html"
	return render(request, template_name, context)























