
document.getElementById('getDataButton').addEventListener('click', fetchData);

function fetchData(myform) {
   fetch("http://localhost:8181/country", {
   })
   .then(response =>  {
    if (!response.ok) {
        throw new Error('Network response was not ok: ' + response.statusText)
    }
    return response.json();
   })
   .then(data => {
    populateTable(data.countries);
    console.log(data.countries);
   })
   .catch(error => console.error('Error fetching data: ', error));
}

function populateTable(countries){
    const tableBody = document.getElementById('data-table').getElementsByTagName('tbody')[0];

   tableBody.innerHTML = "";

    countries.forEach(country => {
    console.log(country);
        const newRow = tableBody.insertRow();

         const idCell = newRow.insertCell(0);
         const nameCell = newRow.insertCell(1);
         const populationCell = newRow.insertCell(2);

         idCell.innerHTML = `<input type="text" readonly value="${country.id}">`;
         nameCell.innerHTML = `<input type="text" value="${country.name}">`;
         populationCell.innerHTML = `<input type="text" value="${country.population}">`;

    });

}