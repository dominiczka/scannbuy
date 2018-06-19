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

$x = $_GET["x"];
$y = $_GET["y"];

$sql = "SELECT ID_SKLEPU, NAZWA, LOKALIZACJA_X, LOKALIZACJA_Y
		FROM SKLEP
        WHERE LOKALIZACJA_X <= $x+1
            AND LOKALIZACJA_X >= $x-1
            AND LOKALIZACJA_Y <= $y+1
            AND LOKALIZACJA_Y >= $y-1;";
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
