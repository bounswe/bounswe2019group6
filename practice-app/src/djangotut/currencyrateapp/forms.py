from django import forms

class SearchForm(forms.Form):
    target     = forms.CharField(label='From EUR to',widget=forms.TextInput(attrs={"placeholder": "Currency"}))