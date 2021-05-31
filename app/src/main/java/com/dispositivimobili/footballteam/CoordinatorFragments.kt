package com.dispositivimobili.footballteam

//interfaccia per coordinare e comunicare i dati tra fragments
interface CoordinatorFragments {

    //metodo per capire quale riga della list view è stata cliccata
    fun onRowClicked(index:Int)

    //metodo per capire quante righe sono presenti nella list view
    fun countList(index: Int)
}