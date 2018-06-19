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

$sql = "SELECT ID_SKLEPU, NAZWA, LOKALIZACJA_X, LOKALIZACJA_Y
		FROM SKLEP;";
$result = $conn->query($sql);

$productList = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
		$product['ID_SKLEPU'] = $row['ID_SKLEPU'];
		$product['NAZWA'] = $row['NAZWA'];

		$product['LOKALIZACJA_X'] = $row['LOKALIZACJA_X'];
		$product['LOKALIZACJA_Y'] = $row['LOKALIZACJA_Y'];

		array_push($productList, $product);
    }
}

echo json_encode($productList);

$conn->close();

?>
