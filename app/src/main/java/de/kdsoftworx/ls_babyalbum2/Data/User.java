package de.kdsoftworx.ls_babyalbum2.Data;


/**
 * Created by Kai on 29.08.2018 by the sweat of one´s brow.
 */

public class User {

    private String useridsm, username, password, salutation, name, surname, street, location, email, dateOfBirth,houseNumber, postCode, phone;
    private int userId;

    public User(Integer userId, String useridsm, String username, String password, String salutation, String name, String surname, String street, String location,
                String email, String dateOfBirth, String houseNumber, String postCode, String phone)
    {
        this.userId = userId;
        this.useridsm = useridsm;
        this.username = username;
        this.password = password;
        this.salutation = salutation;
        this.name = name;
        this.surname = surname;
        this.street = street;
        this.location = location;
        this.email = email;

        this.dateOfBirth = dateOfBirth;
        this.houseNumber = houseNumber;
        this.postCode = postCode;
        this.phone = phone;
    }

    //getter/setter UserID Sozial Media
    public String getUseridsm()
    {
        return useridsm;
    }
    public void setUseridsm(String useridsm)
    {
        this.useridsm = useridsm;
    }

    //getter/setter Username
    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    //getter/setter Password
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    //getter/setter Salutation
    public String getSalutation()
    {
        return salutation;
    }
    public void setSalutation(String salutation)
    {
        this.salutation = salutation;
    }

    //getter/setter Name
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    //getter/setter Surname
    public String getSurname()
    {
        return surname;
    }
    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    //getter/setter Street
    public String getStreet()
    {
        return street;
    }
    public void setStreet(String street)
    {
        this.street = street;
    }

    //getter/setter Location
    public String getLocation()
    {
        return location;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }

    //getter/setter Email
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    //getter/setter DateOfBirth Year
    public String getDateOfBirth()
    {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    //getter/setter Housenumber
    public String getHouseNumber()
    {
        return houseNumber;
    }
    public void setHouseNumber(String houseNumber)
    {
        this.houseNumber = houseNumber;
    }

    //getter/setter Postcode
    public String getPostCode()
    {
        return postCode;
    }
    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    //getter/setter Phone
    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    //getter/setter UserID
    public int getUserId()
    {
        return userId;
    }
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
}

