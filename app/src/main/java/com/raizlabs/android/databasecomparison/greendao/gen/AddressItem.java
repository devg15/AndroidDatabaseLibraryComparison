package com.raizlabs.android.databasecomparison.greendao.gen;

import com.raizlabs.android.databasecomparison.interfaces.IAddressItem;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ADDRESS_ITEM.
 */
public class AddressItem implements IAddressItem<AddressBook> {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private Long phone;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient AddressItemDao myDao;

    private AddressBook addressBook;
    private Long addressBook__resolvedKey;


    public AddressItem() {
    }

    public AddressItem(Long id) {
        this.id = id;
    }

    public AddressItem(Long id, String name, String address, String city, String state, Long phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.phone = phone;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAddressItemDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void setPhone(long phone) {
        setPhone((Long)phone);
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    /** To-one relationship, resolved on first access. */
    public AddressBook getAddressBook() {
        Long __key = this.id;
        if (addressBook__resolvedKey == null || !addressBook__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AddressBookDao targetDao = daoSession.getAddressBookDao();
            AddressBook addressBookNew = targetDao.load(__key);
            synchronized (this) {
                addressBook = addressBookNew;
            	addressBook__resolvedKey = __key;
            }
        }
        return addressBook;
    }

    public void setAddressBook(AddressBook addressBook) {
        synchronized (this) {
            this.addressBook = addressBook;
            id = addressBook == null ? null : addressBook.getId();
            addressBook__resolvedKey = id;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    @Override
    public void saveAll() {
    }
}
