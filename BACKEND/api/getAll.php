<?php

header('Content-Type: application/json');

$servername = "********";
$username = "********";
$password = "********";
$dbname = "********";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT ID_KOD_KRESKOWY, NAZWA, PRODUCENT, CENA, WAGA_GRAMY, KATEGORIA, OPIS, ILOSC_NA_STANIE, ID_SKLEPU
		FROM PRODUKT;";
$result = $conn->query($sql);

$productList = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
		$product['ID_KOD_KRESKOWY'] = $row['ID_KOD_KRESKOWY'];
		$product['NAZWA'] = $row['NAZWA'];
		$product['PRODUCENT'] = $row['PRODUCENT'];
		$product['CENA'] = $row['CENA'];
		$product['WAGA_GRAMY'] = $row['WAGA_GRAMY'];
		$product['KATEGORIA'] = $row['KATEGORIA'];
		$product['OPIS'] = $row['OPIS'];
		$product['ILOSC_NA_STANIE'] = $row['ILOSC_NA_STANIE'];
		$product['ID_SKLEPU'] = $row['ID_SKLEPU'];

		array_push($productList, $product);
    }
}

echo json_encode($productList);

$conn->close();

?>
