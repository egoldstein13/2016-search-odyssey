<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<%@page import="java.sql.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="icon" href="http://cliparts.co/cliparts/8cA/Enp/8cAEnpM6i.png">

    <title>A Search Odyssey</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap2.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/stylish-portfolio1.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,7>00,300italic,400italic,700italic" rel="stylesheet" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Raleway' rel='stylesheet' type='text/css'>


    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->


</head>

<body>

    <!-- Header -->
    <header id="top" class="header">
        <div class="text-vertical-center">
            <h1>2016: A Search Odyssey</h1>
	    <br>
            <form name="searchForm" action="Search" method="POST">
                <input name="search" id="search" type="text" class="form-control" value="" />
                <br>
                <br>
                <button type="submit" class="btn btn-dark btn-lg">Blastoff!</a>
            </form>
        </div>
    </header>

    <!-- Footer -->
    <footer>
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 text-center">
                    <h4><strong>Esther & Hayley are Cool</strong>
                    </h4>
                    <hr class="small">
                    <p class="text-muted">Copyright &copy; 2016: A Search Odyssey</p>
                </div>
            </div>
        </div>
    </footer>

</body>

</html>
