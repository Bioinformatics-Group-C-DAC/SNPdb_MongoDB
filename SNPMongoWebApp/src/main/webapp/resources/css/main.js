/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function getAjaxObject() {

    //alert("getAjaxObject is called");
    var req = false;
    // For Safari, Firefox, and other non-MS browsers
    if (window.XMLHttpRequest) {
        try {
            req = new XMLHttpRequest();
        } catch (e) {
            req = false;
        }
    } else if (window.ActiveXObject) {
        // For Internet Explorer on Windows
        try {
            req = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                req = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
                req = false;
            }
        }
    }

    return req;
}

  function GetSelected () {
    var select = document.getElementById ("form:selectedtbl");
    var txt = "";
    for (var i = 0; i < select.options.length; i++) {
        var isSelected = select.options[i].selected;
        isSelected = (isSelected)? "selected" : "not selected";
        txt += "The " + i + " option is " + isSelected + "\n";
    }
    alert (txt);
}

function  getBlock(){
    alert("-----"+document.getElementById("form").elements["j_idt51:selectedtbl"].value);
    var strByGene= document.getElementById("form").elements["j_idt51:searchByGene"].value;
    var strFormat = document.getElementById("form").elements["j_idt51:searchByFormat"].value;
    var strGeneId = document.getElementById("form").elements["j_idt51:searchgeneidTxt"].value;
    
    var str=document.getElementById("form").elements["j_idt51:selectedtbl"].selectedIndex;
    alert(str);
    if(str==0){
         
    }
    if(str==1){
        
    }
    if(str==2){
        
    }
    if(str==3){
        
    }
    if(str==4 && strFormat=="Unique"){
        url = "UniqueHash.xhtml"; 
    }
    if(str==4 && strFormat=="Common"){
        url = "CommonHash.xhtml"; 
    }
    if(str==5){
        
    }
    if(str==6){
        
    }
    if(str==7){
        
    }
    
    var ajaxObject = getAjaxObject();

    ajaxObject.open("POST",url,false);
    ajaxObject.send(null);
    document.getElementById("contentStatus").innerHTML = "";
    document.getElementById("contentStatus").innerHTML=ajaxObject.responseText;
    alert(url);
    return false;

}