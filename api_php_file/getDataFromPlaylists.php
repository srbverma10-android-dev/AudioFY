<?php
require "DataBase.php";
$db = new DataBase();
if ($db->dbConnect()) {
    $dbLongSongPhoto = $db->getDataForUpperSlide("1");
    print($dbLongSongPhoto);
} else echo "\nError: Database connection";

?>
