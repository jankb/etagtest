
function fetchData() {
   fetch("http://localhost:8181/country", {
   })
   .then(function (response) {
    return response.json();
   })
   .then(function (data) {
    console.log(data);
   })
}