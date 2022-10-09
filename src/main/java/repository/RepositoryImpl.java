package repository;

import model.Clan;

public class RepositoryImpl implements ClanRepository{
    private volatile Buffer buffer;

    public RepositoryImpl(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public synchronized Clan save(Clan clan) {
        System.out.println(14 + "RepositoryImpl.save");
        return (clan);
    }

    @Override
    public Clan findById(Long id) {
        System.out.println("RepositoryImpl.findByid");
        return new Clan();
    }

}
