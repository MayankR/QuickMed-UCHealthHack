# Generated by Django 2.0.4 on 2018-10-07 06:46

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('patients', '0003_auto_20181007_0644'),
    ]

    operations = [
        migrations.AddField(
            model_name='patients',
            name='whats_wrong',
            field=models.CharField(default='Default', max_length=200),
            preserve_default=False,
        ),
    ]
