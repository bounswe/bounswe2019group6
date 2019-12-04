from django import forms

class SearchForm(forms.Form):
    source     = forms.CharField(label='From ',widget=forms.TextInput(attrs={"placeholder": "From Currency"}))
    target     = forms.CharField(label='To',widget=forms.TextInput(attrs={"placeholder": "To Currency"}))